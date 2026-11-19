# Лаборатори №10 — Өгөгдлийн урсгалын тест (Definition–Use pairs)

## Оюутан ба орчин

- Нэр: **Ундрах**
- Код: **B252270135**
- `java -version` бүтэн гаралт:

  ```text
  java version "25.0.2" 2026-01-20 LTS
  Java(TM) SE Runtime Environment (build 25.0.2+10-LTS-69)
  Java HotSpot(TM) 64-Bit Server VM (build 25.0.2+10-LTS-69, mixed mode, sharing)
  ```

- `mvn -version` бүтэн гаралт:

  ```text
  Apache Maven 3.10.0 (c43a36b8d67be7e0805a411bc0898af1a51f5472)
  Maven home: /opt/homebrew/Cellar/maven/3.10.0/libexec
  Java version: 27.0.1.0.3, vendor: Homebrew, runtime: /opt/homebrew/Cellar/openjdk/27.0.1.0.3/libexec/openjdk.jdk/Contents/Home
  Default locale: en_MN, platform encoding: UTF-8, time zone: Asia/Ulaanbaatar
  OS name: "mac os x", version: "26.2", arch: "aarch64", family: "mac"
  ```

- Starter Java compile target: `17`
- JUnit Jupiter `5.10.2`; JaCoCo `0.8.12`.
- PMD `7.14.0`; M3 static analysis-г Java `25.0.2` runtime дээр ажиллуулсан.

## Starter

`src/main/java/mn/edu/must/sqat/DataFlowTargets.java` файлыг starter.zip-ээс өөрчлөлгүй оруулсан. `mvn verify`-г эхлүүлж starter төслийн `BUILD SUCCESS`-ийг `results/mvn-verify-starter.txt`-д хадгалсан.

## A. `gcd` — бүх DU хос

CFG-ийн товчлол: 13-д `x`, `y` параметрүүд тодорхойлогдоно; 15 нь `y != 0` predicate; 16-д `tmp` тодорхойлогдож `x`,`y`-г c-use хийнэ; 17-д `x` шинэчлэгдэнэ; 18-д `y` шинэчлэгдэнэ; 20-д `x` буцаагдана. Def-clear гэдэг нь def мөрөөс use хүртэлх замд тухайн хувьсагч дахин тодорхойлогдохгүй байхыг хэлнэ.

| Хувьсагч | Def мөр | Use мөр | Use төрөл | Def-clear зам | Тестийн ID | Хамрагдсан |
|---|---:|---:|---|---|---|---|
| x | 13 | 16 | c-use | 13→15(T)→16 | GCD-01 | Тийм |
| x | 13 | 20 | c-use | 13→15(F)→20 | GCD-02 | Тийм |
| x | 17 | 16 | c-use | 17→18→15(T)→16 | GCD-01 | Тийм |
| x | 17 | 20 | c-use | 17→18→15(F)→20 | GCD-01 | Тийм |
| y | 13 | 17 | c-use | 13→15(T)→16→17 | GCD-01 | Тийм |
| y | 13 | 15 | p-use | 13→15(F) | GCD-02 | Тийм |
| y | 13 | 15 | p-use | 13→15(T)→16→17→18→15 | GCD-01 | Тийм |
| y | 13 | 16 | c-use | 13→15(T)→16 | GCD-01 | Тийм |
| y | 18 | 17 | c-use | 18→15(T)→16→17 | GCD-01 | Тийм |
| y | 18 | 15 | p-use | 18→15(T) | GCD-01 | Тийм |
| y | 18 | 15 | p-use | 18→15(F) | GCD-01 | Тийм |
| y | 18 | 16 | c-use | 18→15(T)→16 | GCD-01 | Тийм |
| tmp | 16 | 18 | c-use | 16→17→18 | GCD-01 | Тийм |

Хүснэгт дэх мөрийн дугаарууд нь starter файлын өөрчлөгдөөгүй бодит мөрүүд. `GCD-01` олон давталттай замаар loop доторх def-үүдийг, `GCD-02` тэг давталтын parameter-use замыг хамарна.

## B. `bonus` — def/use жагсаалт ба DU analysis

### B2. Мөр бүрийн def/use жагсаалт

| Мөр | Def | Use (төрөл) |
|---:|---|---|
| 31 | `attendance`, `threshold` параметрүүд | — |
| 32 | — | `attendance` (p-use), `threshold` (p-use; `attendance != null` үед) |
| 33 | — | — |
| 35 | `present` | — |
| 36 | `streak` | — |
| 37 | `best` | — |
| 38 | `a` (давталтын утга) | `attendance` (p-use: дараагийн элемент байгаа эсэх) |
| 39 | — | `a` (p-use: true болон false үр дүн) |
| 40 | `present` | `present` (c-use, `present + 1`) |
| 41 | `streak` | `streak` (c-use, `streak + 1`) |
| 42 | — | `streak` (p-use), `best` (p-use) |
| 43 | `best` | `streak` (c-use) |
| 45 | — | — |
| 46 | `streak` | — |
| 48 | — | — |
| 49 | `score` | — |
| 50 | — | `present` (p-use), `threshold` (p-use) |
| 51 | `score` | — |
| 53 | — | `best` (p-use) |
| 54 | `score` | `score` (c-use, `score + 3`) |
| 56 | — | `score` (c-use, return) |

### B3. Def-clear DU хосууд

| Хувьсагч | Def мөр | Use мөр | c-use / p-use | Def-clear зам | Тестийн ID |
|---|---:|---:|---|---|---|
| attendance | 31 | 32 | p-use | 31→32 | B-AD-01, B-INVALID-01 |
| attendance | 31 | 38 | p-use | 31→32(false)→35→38 | B-AD-01, B-DU-03 |
| threshold | 31 | 32 | p-use | 31→32 (attendance null биш үед) | B-INVALID-02, B-AD-01 |
| threshold | 31 | 50 | p-use | 31→32(false)→35→38→49→50 | B-AD-01, B-DU-02/03 |
| present | 35 | 40 | c-use | 35→38→40 | B-AD-01 |
| present | 35 | 50 | p-use | 35→38(false)→49→50 | B-AD-02, B-DU-03 |
| present | 40 | 40 | c-use | 40→41→42→38→39→40 | B-AD-01 |
| present | 40 | 50 | p-use | 40→…→38(false)→49→50 | B-AD-01, B-DU-02 |
| streak | 36 | 41 | c-use | 36→37→38→39(true)→40→41 | B-AD-01 |
| streak | 41 | 41 | c-use | 41→42→38→39(true)→40→41 | B-AD-01 |
| streak | 41 | 42 | p-use | 41→42 | B-AD-01, B-DU-02 |
| streak | 41 | 43 | c-use | 41→42(true)→43 | B-AD-01 |
| streak | 46 | 41 | c-use | 46→38→39(true)→40→41 | B-AD-01 |
| best | 37 | 42 | p-use | 37→38→39(true)→40→41→42 | B-AD-01 |
| best | 37 | 53 | p-use | 37→…→38(false)→49→50→53 | B-AD-01, B-DU-02/03 |
| best | 43 | 42 | p-use | 43→38→39(true)→40→41→42 | B-AD-01 |
| best | 43 | 53 | p-use | 43→…→38(false)→49→50→53 | B-AD-01 |
| score | 49 | 54 | c-use | 49→50(false)→53(true)→54 | B-DU-02 |
| score | 49 | 56 | c-use | 49→50(false)→53(false)→56 | B-DU-03 |
| score | 51 | 54 | c-use | 51→53(true)→54 | B-AD-01 |
| score | 51 | 56 | c-use | 51→53(false)→56 | B-DU-04 |
| score | 54 | 56 | c-use | 54→56 | B-AD-01 |
| a | 38 | 39 (true) | p-use | 38→39(true) | B-AD-01, B-DU-02 |
| a | 38 | 39 (false) | p-use | 38→39(false) | B-AD-01, B-DU-02 |

Биелэшгүй DU хосын жишээ: `streak`-ийн мөр 36 def → мөр 42 p-use. Эхний элемент эерэг байвал мөр 42-оос өмнө мөр 41 `streak`-ийг дахин тодорхойлно; эхний элемент эерэг биш бол мөр 42-т хүрэхгүй. Иймээс def-clear зам байхгүй. Үүнтэй адил мөр 46 def → мөр 42 p-use болон эдгээр def-үүдээс мөр 43 c-use руу def-clear зам байхгүй. Мөр 46-ын дараа дараагийн эерэг элементэд мөр 41 эхэлж шинэ def хийдэг.

### B1. Bonus CFG

Зангилаанууд starter файлын мөрийн дугаартай; ирмэгүүдэд түүгээр дайрах тестийн ID тэмдэглэсэн.

![bonus CFG](cfg-bonus.png)

### B4. ALL-DEFS шат

`B-AD-01`-ийн attendance-д 6 өдөр ирсэн тул `present >= threshold` үнэн болж мөр 51 `score=5`-аар мөр 49-ийн `score=0` тодорхойлолтыг use-д хүрэхээс өмнө дарна. Тиймээс `B-AD-01` дангаараа all-defs биш. `B-AD-02` хоосон attendance, эерэг threshold ашиглаж мөр 49-ийн `score=0`-г мөр 56 return-д хүргэнэ. Эдгээр хоёр тест хамтдаа all-defs-д шаардлагатай хамгийн бага тоо **2** болно. `mvn clean verify -Dtest='GcdTest,BonusTest#positiveAttendanceReachesScoreDefinitionAt51+scoreInitializationFlowsDirectlyToReturn'` гаралт болон JaCoCo CSV-г `results/mvn-verify-alldefs.txt`, `results/jacoco-alldefs.csv`-д хадгална.

Энэ шатны run нь GCD-ийн 2 тест болон bonus-ийн `B-AD-01`, `B-AD-02` гэсэн 2 тест ажиллуулж, **4 тест, 0 failure/error** гаргасан. JaCoCo class row: **14/16 branches**, 22/24 lines. ALL-DEFS нь branch coverage-тэй адил шалгуур биш тул ALL-DU-PAIRS шатанд нэмэлт замууд орно.

### B5. ALL-DU-PAIRS шат

`BonusTest` одоо `B-AD-01` дээр нэмээд DU хүснэгтийн бүх замд хүрэх 5 тест агуулна: `B-DU-02` score-ийн def-to-use хоёр зам болон fragmented streak; `B-DU-03` хоосон массив/score буцах зам; `B-DU-04` threshold хангаж байгаа ч streak < 5 зам; `B-INVALID-01/02` null attendance болон сөрөг threshold. GCD-ийн 2 тесттэй нийлээд бүх suite **8 тест** ажиллуулна. `mvn clean verify`-ийн JaCoCo үр дүн class row дээр **BRANCH_MISSED=0, BRANCH_COVERED=16** болохыг эцсийн CSV-ээр батална.

## C. Fault seeding үр дүн

Мутаци бүрийг тусад нь оруулж `mvn test` ажиллуулаад `git restore`-оор starter эх кодыг шууд буцаасан. Эцсийн `DataFlowTargets.java` өөрчлөгдөөгүй.

| Fault | Үр дүн | Гаралтын файл |
|---|---|---|
| M1 — `streak = streak + 1` мөрийг хасах | **Баригдсан:** `positiveAttendanceReachesScoreDefinitionAt51` — expected 8, was 5; `scoreInitializationFlowsToStreakBonus` — expected 3, was 0. `BUILD FAILURE`, exit 1 | `results/mvn-test-M1.txt` |
| M2 — `best = streak` → `best = present` | Эхний багцад амьд үлдсэн (`BUILD SUCCESS`): `B-AD-01`-д `present=6`, `best=5` тул хоёр хувилбарт хоёуланд нь 3 онооны streak bonus нэмэгдэнэ; `B-DU-02`-т `present=best=5`, `B-DU-03`-т хоосон массив байна. `present>=5` мөртөө `best<5` нөхцөлийг эхний багц шалгаагүй. `B-DU-04` `[1,0,1,1,1,1,0]`, threshold 5 гэж өгч `present=5`, `best=4` болгоход **баригдсан**: expected 5, was 8; нэг failure, exit 1 | `results/mvn-test-M2.txt`, `results/mvn-test-M2-kill.txt` |
| M3 — ашиглагдахгүй `int total = present + best;` тодорхойлолт нэмэх | Unit тестүүд **8/8 PASS**, exit 0. PMD-ийн `UnusedLocalVariable` дүрэм `total`-ийг 49-р мөрөнд илрүүлж `pmd:check`-ийг нэг violation-оор унагасан | `results/mvn-test-M3.txt`, `results/pmd-M3.txt`, `results/pmd-M3.xml` |

PMD-г дахин шалгахдаа fault түр идэвхтэй байхад дараах командыг ажиллуулна:

```sh
JAVA_HOME="/Library/Java/JavaVirtualMachines/jdk-25.jdk/Contents/Home" \
  PATH="/Library/Java/JavaVirtualMachines/jdk-25.jdk/Contents/Home/bin:$PATH" \
  mvn pmd:check
```

Final restore-ийн дараа `mvn clean verify` ногоон болсон; `results/mvn-verify-final.txt`, `results/jacoco-final.csv` нь эх код сэргээсэн эцсийн төлөвийг харуулна.

## Дүгнэлт

1. Def/use хүснэгт нь хувьсагч хаана утга авч, хаана predicate эсвэл тооцоонд хэрэглэгдэж байгааг мөрөөр нь холбосон.
2. GCD-ийн хоёр тест loop body хэрэгжих болон хоосон loop exit замын бүх DU хосыг хамарсан.
3. `B-AD-01`-ийн true threshold branch score@49 def-ийг дардаг тул all-defs-д B-AD-01 болон B-AD-02 гэсэн хоёр тест шаардлагатай.
4. Нэмэлт bonus тестүүд score-ийн c-use болон predicate use-ийн өөр өөр def-clear замыг ялгаж шалгасан.
5. Мөр 46 дээрх `streak=0` нь өмнөх streak def-үүдийг kill хийж, дараагийн streak тооцоог тасалдаг.
6. Бүрэн suite 8 тестээр JaCoCo-ийн 16 branch бүгдийг хамарсан ч branch coverage DU хос бүр хүрснийг батлахгүй. Жишээ нь B-DU-02 дангаараа `present@35→50`-г хамрахгүй; B-DU-03-ийн хоосон массив энэ def-clear замыг хадгална. `streak@36→42` нь мөр 41-ийн дахин def-ээс болж биелэшгүй.
7. M1 missing update алдааг streak-ийн хүлээгдэх оноо өөрчлөгдсөнөөр тестүүд барьсан.
8. M2 эхэндээ амьд үлдсэн тул төлөөлөх input-ийг шинэчилсний дараа mutation-ийн нөлөөг онооны босгон дээр ялгаж барьсан.
9. M3-ийн dead definition нь runtime behavior-д нөлөөлөөгүй ч PMD-ийн unused-local-variable шинжилгээгээр илэрсэн.
