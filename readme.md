[![Codacy Badge](https://api.codacy.com/project/badge/Grade/75a743b7e2e2446a804945ad488f74ca)](https://www.codacy.com/app/tandser/logreader?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=tandser/logreader&amp;utm_campaign=Badge_Grade)
[![Dependency Status](https://dependencyci.com/github/tandser/magnet/badge)](https://dependencyci.com/github/tandser/magnet)

# Сборка проекта

`mvn clean compile assembly:single`

# Запуск приложения

## Unix

`cat access.log | java -jar logreader-1.0.0.jar -u 99.9 -t 45`

## Windows

`type access.log | java -jar logreader-1.0.0.jar -u 99.9 -t 45`