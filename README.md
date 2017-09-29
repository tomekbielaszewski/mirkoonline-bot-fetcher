# mirkoonline-bot-fetcher [![Build Status](https://travis-ci.org/tomekbielaszewski/mirkoonline-bot-fetcher.svg)](https://travis-ci.org/tomekbielaszewski/mirkoonline-bot-fetcher)

Wylicza podstawowe statystyki aktywności użytkowników na wykop.pl/mikroblog

## Zależności

Do działania wymaga:
- https://github.com/tomekbielaszewski/mirko-fetcher
- https://github.com/tomekbielaszewski/keeper
- MongoDB

## Użycie

`java -jar mirkoonline-bot-fetcher-0.2.3-SNAPSHOT.jar --fetch-last-x-hours 24 --count-last-x-minutes 30`
`java -jar mirkoonline-bot-fetcher-0.2.3-SNAPSHOT.jar -fetch 24 -count 30`

Gdzie:
- `--fetch-last-x-hours` / `-fetch` - to liczba ostatnich godzin z których pobierane są wpisy. Na przykład `-fetch 5` wywołane o godz 23:30 pobierze wpisy do godziny 18:30
- `--count-last-x-minutes` / `-count` - to liczba ostatnich minut aktywności branych pod uwage. Na przykład gdy pobrano `-fetch 24` z `-count 30` wtedy brane są pod uwage wszystkie wpisy z ostatnich 30 minut jak i wszystkie komentarze i plusy z ostatnich 30 minut pod wpisami z ostatnich 24h.