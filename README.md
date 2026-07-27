# slot-game-engine-service

Pure, stateless slot game math engine — the certified RNG + reels + paytable + bonus-games core of the `slot-central` microservices platform.

This service performs only **pure spin computation**: given a bet/denom context, it draws reel stops (via CSPRNG), resolves primary line wins, evaluates bonus-game eligibility, and returns a full win/result payload. It has **no side effects, no database calls, no wallet mutation**.

This is a re-architecture of the `slot-central-server-express-rmq` Node.js EGM slot-floor backend into Spring Boot microservices.

---

## Role in the Platform

```
slot-api-gateway
    └── slot-game-controller-service  ← orchestrates a full spin round
            ├── slot-bank-service            (balance reserve/settle)
            ├── slot-game-engine-service     ← THIS SERVICE
            ├── slot-jackpot-service         (jackpot pool state)
            └── slot-logging-service
```

`slot-game-controller-service` calls `POST /api/v1/spin/compute` with `{spinId, gameId, betIndex, denomIndex}`. This service computes and returns the full result; the controller then handles bank settlement and jackpot pool updates.

---

## Quick Start

### Prerequisites
- Java 21+
- Maven 3.9+

### Run locally
```bash
mvn spring-boot:run
```
Service starts on `http://localhost:8084`.

### Build & test
```bash
mvn clean install        # compiles, tests, packages
mvn test                 # tests only (33 tests, ~14s)
```

### Docker
```bash
docker build -t slot-game-engine-service .
docker-compose up
```

---

## API

### `POST /api/v1/spin/compute`

Compute a spin result.

**Request body:**
```json
{
  "spinId": "uuid-or-string",
  "gameId": "CLASSIC_3REEL",
  "betIndex": 2,
  "denomIndex": 4,
  "egmId": "EGM-001",
  "includeTrace": false,
  "reelStopOverrides": { "s1": 1, "s2": 5, "s3": 12 }
}
```
- `betIndex`: 0–4 (maps to bets [1,2,3,4,5])
- `denomIndex`: 0–8 (maps to denoms [1,2,5,10,20,50,100,200,500])
- `includeTrace`: when `true`, response includes raw RNG stop values (for math/RTP testing)
- `reelStopOverrides`: **only honoured when `gaffing.enabled=true`** (QA/test use only)

**Response:**
```json
{
  "spinId": "uuid-or-string",
  "gameId": "CLASSIC_3REEL",
  "egmId": "EGM-001",
  "betIndex": 2,
  "denomIndex": 4,
  "betAmount": 60,
  "reel1Stop": 42,
  "reel2Stop": 17,
  "reel3Stop": 99,
  "symbol1": "CHERRY",
  "symbol2": "PLUM",
  "symbol3": "BAR",
  "primaryWinAmount": 120,
  "cyclicWinArr": [0],
  "anticipationFlag": false,
  "jackpot1Win": false,
  "jackpot2Win": false,
  "jackpot3Win": false,
  "jackpot4Win": false,
  "jackpotContribution": 0.3,
  "bonusGameResult": {
    "gameType": "NONE",
    "winData": [],
    "totalWin": 0
  },
  "totalWinAmount": 120,
  "spinTrace": null
}
```

### `GET /api/v1/paytable`
Returns the current effective paytable/bet-denom configuration.

### `GET /actuator/health`
Spring Boot Actuator health endpoint.

### `GET /actuator/info`
Service metadata.

---

## Configuration

All game math configs are externalised under `src/main/resources/gamemath/` so they can be swapped/audited independently of code changes (a key regulatory requirement):

| File | Contents |
|---|---|
| `reel1-strip.yml` | 128-position virtual reel strip for Reel 1 |
| `reel2-strip.yml` | 128-position virtual reel strip for Reel 2 |
| `reel3-strip.yml` | 128-position virtual reel strip for Reel 3 |
| `money-wheel.yml` | Money Wheel bonus segment weights/payouts |
| `hit-or-miss.yml` | Hit-or-Miss bonus hit probability/win amounts |
| `one-of-three.yml` | One-of-Three bonus option payouts |
| `six-of-eighteen.yml` | Six-of-Eighteen bonus reveal pool config |

Paytable (bet/denom matrix, symbol payouts, jackpot contribution rate) is configured directly in `application.yml` under the `paytable:` key.

---

## Environment Variables

| Variable | Default | Description |
|---|---|---|
| `SERVER_PORT` | `8084` | HTTP port |
| `GAFFING_ENABLED` | `false` | Enable reel-stop overrides in API requests (**test/QA only — must be `false` in production**) |
| `SPRING_PROFILES_ACTIVE` | _(none)_ | Use `prod` for JSON structured logging |

---

## Gaffing / Test Overrides

The original Node.js monolith supported `isGaffingOn` + `s1/s2/s3` reel-stop override params for deterministic test spins (QA and math verification). This service mirrors that behaviour via the `reelStopOverrides` request field, gated by the `gaffing.enabled` feature flag.

**When `gaffing.enabled=false` (production default):** `reelStopOverrides` fields in API requests are silently ignored. Real CSPRNG draws are always used.

**When `gaffing.enabled=true` (QA/test environments only):** Supplied `s1/s2/s3` stop indices are used directly, making the spin fully deterministic and reproducible for testing.

> ⚠️ **PRODUCTION REQUIREMENT**: `GAFFING_ENABLED` must be `false` (or absent) in all production/certified deployments. Enabling gaffing in a live environment violates EGM regulatory requirements.

---

## RNG Design

All randomness flows exclusively through `com.slotcentral.gameengine.rng.RandomNumberGenerator`, which wraps `java.security.SecureRandom`. No other class in this service instantiates `Random`, `SecureRandom`, or calls `Math.random()` directly.

**This is a deliberate fix over the original Node.js monolith**, which used `Math.random()` (a non-cryptographically-secure PRNG) in `getRandomInt(min, max)` within `src/services/random.js`. That was a regulatory defect: all win-determination randomness paths must use a CSPRNG. This service uses `SecureRandom` exclusively for all RNG paths.

---

## Jackpot State Ownership (Deliberate Deviation from Monolith)

The original monolith (`game-service/index.js`) maintained the jackpot pool ticker (`JDTicker`) directly in the game service, incrementing it by `betAmount * JDContrib (0.5%)` on every non-jackpot spin and resetting it after a win.

**This service does NOT own jackpot pool state.** It returns:
- `jackpot1Win`, `jackpot2Win`, `jackpot3Win`, `jackpot4Win` — boolean flags indicating whether each jackpot tier was triggered
- `jackpotContribution` — the computed contribution amount (`betAmount * 0.005`) for the caller to apply

The `slot-jackpot-service` is responsible for maintaining the pool tickers, incrementing on contribution, and paying out on trigger. This service is a pure, stateless math core — maintaining mutable pool state here would violate the stateless/no-side-effects constraint and make regulatory audit harder.

---

## ⚠️ ASSUMPTIONS MADE

Because the exact real-money payout tables, reel strips, and bonus game math specs from the certified monolith were not fully available for inspection, the following are **placeholder/assumed values** that **MUST be validated against the real math specification before any real-money certification**:

### Reel Strips
- Three 128-position virtual reel strips were designed with realistic symbol frequency distributions (more BLANK/CHERRY, fewer SEVEN/BONUS) to target a plausible RTP range.
- **The exact symbol positions on each strip are assumed** — the real certified strips must be supplied and loaded via the YAML files without code changes.

### Primary Game Paytable (Symbol Payouts)
The following multipliers (applied to `betAmount`) are assumed:

| Combination | Multiplier | Notes |
|---|---|---|
| SEVEN SEVEN SEVEN | 2000× | Jackpot-tier win |
| BAR3 BAR3 BAR3 | 500× | Triple-bar top |
| BAR2 BAR2 BAR2 | 200× | |
| BELL BELL BELL | 100× | |
| BAR BAR BAR | 100× | |
| any 3 mixed BARs | 50× | BAR/BAR2/BAR3 any combination |
| PLUM PLUM PLUM | 40× | |
| ORANGE ORANGE ORANGE | 30× | |
| LEMON LEMON LEMON | 20× | |
| CHERRY CHERRY CHERRY | 20× | |
| CHERRY CHERRY (any) | 10× | Two cherries |
| CHERRY (any) (any) | 2× | One cherry in position 1 |

- **These multipliers are assumed.** The real certified paytable must be supplied.
- Single-cherry multiplier was set to **2×** (reduced from a higher value) to keep the 100k-spin RTP simulation within a plausible range (~80% theoretical RTP with placeholder strips).

### Jackpot Tier Triggers (Assumed)
- **Jackpot 1**: SEVEN SEVEN SEVEN at max bet (betIndex=4) — grand jackpot
- **Jackpot 2**: SEVEN SEVEN SEVEN at any bet
- **Jackpot 3**: BAR3 BAR3 BAR3 at max bet (betIndex=4)
- **Jackpot 4**: BONUS BONUS BONUS

### Bonus Game Trigger Conditions (Assumed)
| Symbol Combination | Bonus Game Triggered |
|---|---|
| BONUS BONUS BONUS | Six-of-Eighteen |
| BONUS BONUS (any) | One-of-Three |
| BONUS (any) (any) | Hit-or-Miss |
| BELL BELL BELL | Money Wheel |
| anything else | No bonus |

### Bonus Game Payouts
All bonus game payout tables (`money-wheel.yml`, `hit-or-miss.yml`, `one-of-three.yml`, `six-of-eighteen.yml`) contain **assumed/placeholder values**. These must be validated against the real math spec.

### Anticipation Flag
`anticipationFlag=true` when reel 1 and reel 2 show SEVEN+SEVEN or BONUS+BONUS — indicating to the UI that a big win is possible on reel 3 (for animation purposes). The exact anticipation trigger conditions from the monolith may differ.

---

## RTP Simulation Smoke Test

`src/test/java/com/slotcentral/gameengine/simulation/RtpSimulationTest.java` runs 100,000 simulated spins at betIndex=2, denomIndex=4 (`betAmount=60`) using real SecureRandom draws (no gaffing) and asserts the resulting RTP falls within the range **75%–99%**.

> ⚠️ **This is a smoke test only, not a certification-grade math proof.** The placeholder reel strips and paytable were tuned to produce a plausible RTP range, but the actual certified RTP target (e.g. 94.5%) must be validated by a licensed gaming math lab against the real strips and paytable.

---

## TODO Before Real-Money Certification

1. **Replace placeholder reel strips** — load the certified reel strip arrays (signed/hashed) into `gamemath/reel1-strip.yml`, `reel2-strip.yml`, `reel3-strip.yml`. Do not change any Java code.
2. **Replace placeholder paytable** — update symbol payout multipliers in `application.yml` under `paytable.symbol-payouts` to match the certified math spec.
3. **Validate and replace bonus game configs** — update all four bonus game YAML files with certified payout tables.
4. **Commission RTP verification** — have a licensed gaming math lab run a full RTP analysis against the real strips + paytable.
5. **Lock down gaffing** — remove the `gaffing.enabled` config path entirely from certified builds, or gate it behind a compile-time flag that is excluded from the certified binary.
6. **Add RNG audit trail persistence** — the `SpinTrace` (raw RNG draws per spin) infrastructure is in place. Wire it to a persistent audit log (append-only, tamper-evident) before certification.
7. **Regulatory RNG certification** — submit `RandomNumberGenerator` (SecureRandom-based) for independent RNG certification if required by the jurisdiction.
8. **Fix `Math.random()` usage audit** — run a static analysis scan (`grep -r "Math.random\|new Random()"`) to confirm no regressions introduce non-CSPRNG randomness.
9. **Security review of gaffing endpoint** — ensure gaffing cannot be enabled via runtime config in production deployments (e.g., immutable config, read-only filesystem, sealed Kubernetes secrets).

---

## Project Structure

```
src/main/java/com/slotcentral/gameengine/
├── GameEngineApplication.java
├── api/
│   ├── CorrelationIdFilter.java     # X-Correlation-Id MDC propagation
│   ├── GaffingProperties.java       # gaffing.enabled config property
│   ├── ReelStopOverrides.java       # s1/s2/s3 override DTO
│   ├── SpinController.java          # REST endpoints
│   ├── SpinRequest.java             # Request DTO
│   ├── SpinResponse.java            # Response DTO
│   └── SpinService.java             # Spin computation orchestration
├── paytable/
│   ├── PaytableConfig.java          # @ConfigurationProperties binding
│   ├── PaytableService.java         # betAmount/jackpotContrib lookup
│   └── SymbolPayoutTable.java       # symbol win multiplier lookup
├── primary/
│   ├── PrimaryGameEngine.java       # win/jackpot/anticipation logic
│   └── PrimaryGameResult.java      # result record
├── reels/
│   ├── AbstractReel.java            # strip loading + symbol lookup
│   ├── Reel1.java / Reel2.java / Reel3.java
│   ├── Reel1Properties.java / Reel2Properties.java / Reel3Properties.java
│   ├── ReelComponent.java           # interface
│   └── ReelStripConfig.java         # YAML binding POJO
├── rng/
│   └── RandomNumberGenerator.java   # SecureRandom wrapper (sole RNG)
└── secondary/
    ├── BonusGame.java               # interface
    ├── BonusGameResult.java / WinEntry.java
    ├── HitOrMissGame.java / HitOrMissConfig.java
    ├── MoneyWheelGame.java / MoneyWheelConfig.java
    ├── OneOfThreeGame.java / OneOfThreeConfig.java
    ├── SecondaryGameEngine.java     # bonus trigger dispatch
    └── SixOfEighteenGame.java / SixOfEighteenConfig.java

src/main/resources/
├── application.yml                  # main config (paytable, gaffing, actuator)
├── logback-spring.xml               # JSON logging for prod profile
└── gamemath/                        # all certifiable game math configs
    ├── reel1-strip.yml
    ├── reel2-strip.yml
    ├── reel3-strip.yml
    ├── money-wheel.yml
    ├── hit-or-miss.yml
    ├── one-of-three.yml
    └── six-of-eighteen.yml
```
