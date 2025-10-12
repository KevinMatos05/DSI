# run.ps1 - Ejecuta la app de forma automática
# Comportamiento:
# 1) Si existe 'mvn', ejecuta: mvn clean javafx:run
# 2) Si no, busca JAVAFX_HOME o C:\javafx-sdk-17.0.2\lib y compila/ejecuta usando javac/java

$projectRoot = Split-Path -Parent $MyInvocation.MyCommand.Definition
Write-Host "Proyecto: $projectRoot"

# Preferir usar JavaFX SDK local si está disponible
# Buscar JAVAFX_HOME o ruta por defecto
$javafxLib = $env:JAVAFX_HOME
if ([string]::IsNullOrEmpty($javafxLib)) {
    $candidate = 'C:\\javafx-sdk-17.0.2\\lib'
    if (Test-Path $candidate) { $javafxLib = $candidate }
}

if ($javafxLib -and (Test-Path $javafxLib)) {
    Write-Host "JavaFX SDK encontrado en: $javafxLib -> usaré javac/java para compilar y ejecutar"

    if (-not (Get-Command java -ErrorAction SilentlyContinue)) {
        Write-Error "java no está disponible en PATH. Instala un JDK y vuelve a intentarlo."
        exit 1
    }
    if (-not (Get-Command javac -ErrorAction SilentlyContinue)) {
        Write-Error "javac no está disponible en PATH. Instala el JDK completo y vuelve a intentarlo."
        exit 1
    }

    # Rutas
    $srcDir = Join-Path $projectRoot 'src\main\java'
    $outDir = Join-Path $projectRoot 'target\classes'
    if (-not (Test-Path $outDir)) { New-Item -ItemType Directory -Path $outDir -Force | Out-Null }

    # Encontrar todos los .java
    $files = Get-ChildItem -Path $srcDir -Recurse -Filter *.java | ForEach-Object { $_.FullName }
    if ($files.Count -eq 0) {
        Write-Error "No se encontraron archivos .java en $srcDir"
        exit 3
    }

    # Compilar
    $moduleArgs = "--module-path `"$javafxLib`" --add-modules javafx.controls,javafx.fxml"
    $javacCmd = "javac $moduleArgs -d `"$outDir`" " + ($files -join ' ')
    Write-Host "Compilando..."
    Write-Host $javacCmd
    Invoke-Expression $javacCmd
    if ($LASTEXITCODE -ne 0) {
        Write-Error "javac devolvió código $LASTEXITCODE"
        exit $LASTEXITCODE
    }

    # Ejecutar
    Write-Host "Ejecutando MainApp..."
    $runCmd = "java $moduleArgs -cp `"$outDir`" com.example.MainApp"
    Write-Host $runCmd
    Invoke-Expression $runCmd
    exit $LASTEXITCODE
}

# Si no hay JavaFX SDK local, intentar usar mvn si está disponible
if (Get-Command mvn -ErrorAction SilentlyContinue) {
    Write-Host "Maven detectado. Ejecutando: mvn clean javafx:run"
    Push-Location $projectRoot
    mvn clean javafx:run
    Pop-Location
    exit $LASTEXITCODE
}

Write-Host "Ni Maven ni JavaFX SDK local detectados. Por favor instala Maven o descarga JavaFX SDK 17.0.2 y define JAVAFX_HOME o descomprímelo en C:\\javafx-sdk-17.0.2."
exit 4
