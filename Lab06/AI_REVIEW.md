# Lab 06 – AI evaluation of the logging strategy

## Logging plan (from the lab sheet)

| Event | Level | Where |
|---|---|---|
| Application starts / ends | INFO | `App.main` |
| Wallet account created | DEBUG | `WalletAccount` constructor |
| Deposit / withdraw | DEBUG | `WalletAccount.deposit` / `withdraw` |
| InsufficientFundsException created | WARN | `InsufficientFundsException` constructor |
| Thrown exception (caught) | ERROR | `catch` blocks in `App.main` |

## Which messages appear at each level

| `log4j.rootLogger` | What is logged |
|---|---|
| ERROR | only the two "failed" messages |
| WARN | + "InsufficientFundsException created" |
| INFO | + "Application is starting / ending" |
| DEBUG | everything, including account creation, deposit and withdraw |

## AI recommendations (applied)

1. **Use parameterized messages (`{}`) instead of string concatenation.**
   The message is only built if the level is enabled, so DEBUG logging costs nothing when it is turned off.
2. **Pass the exception object as the last argument to `logger.error(...)`.**
   `logger.error("Withdrawal failed", e)` records the full stack trace. Logging only `e.getMessage()` loses where the error came from.
3. **Declare loggers as `private static final`, one per class.**
   This creates one logger per class, stops other classes from replacing it, and makes `%C` show the correct class.
4. **Replace every `System.out.println` with a logger call.**
   This puts all output under one level setting and one format, and sends it to the log file.
5. **Log the "requested" and "successful" step of each operation separately.**
   When a deposit or withdrawal fails, the log still shows what was attempted and the balance at that moment.
6. **Add a console appender next to the file appender.**
   You can see the logs while the program runs, and the file keeps a permanent copy.
7. **Log the final balance when the application ends.**
   This gives a one-line summary that is easy to check at the end of a run.

## Recommendations noted but not applied

- **Logging when an exception object is created (WARN) is unusual.** Normally you log where the exception is handled, not where it is constructed, because an exception can be created and never thrown. It is kept here only because the lab sheet asks for it.
- **Use `BigDecimal` instead of `double` for money** to avoid rounding errors. It is left out because it is outside this lab's scope.
- **`slf4j-log4j12` 2.x is relocated to `slf4j-reload4j`,** because log4j 1.x is end-of-life and has known vulnerabilities. For a real project, use logback or log4j2.
