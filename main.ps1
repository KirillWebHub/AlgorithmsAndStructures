# 1) Найдём все ваши исходники в папке backend
$src = Get-ChildItem -Path .\backend -Recurse -Filter *.java |
Select-Object -ExpandProperty FullName

# 2) Создадим папку для .class (если ещё нет)
if (!(Test-Path -Path .\out)) { New-Item -ItemType Directory .\out }

# 3) Скомпилируем _все_ источники разом, направив .class в out
javac -d out $src
