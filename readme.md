# Сборка проекта

`mvn clean compile assembly:single`

# Запуск приложения

## Unix

`cat access.log | java -jar logreader-1.0.0.jar -u 99.9 -t 45`

## Windows

`type access.log | java -jar logreader-1.0.0.jar -u 99.9 -t 45`