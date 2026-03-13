param(
    [Parameter(Position=0)]
    [string]$Mode = "dev"
)

# Cores para output
$Green = "Green"
$Yellow = "Yellow"
$Cyan = "Cyan"
$Red = "Red"

Write-Host "===================================" -ForegroundColor $Cyan
Write-Host " Construindo a imagem Docker da aplicação..." -ForegroundColor $Green
Write-Host "===================================" -ForegroundColor $Cyan

# Executar comando baseado no modo
if ($Mode -eq "prod") {
    Write-Host " Modo produção" -ForegroundColor $Yellow
    $result = docker-compose -f docker-compose.yml -f docker-compose.prod.yml up -d --build
} elseif ($Mode -eq "dev") {
    Write-Host " Modo desenvolvimento" -ForegroundColor $Cyan
    $result = docker-compose up --build
} else {
    Write-Host "🔧 Modo padrão" -ForegroundColor $Yellow
    $result = docker-compose up --build
}

# Verificar se o comando foi bem-sucedido
if ($LASTEXITCODE -eq 0) {
    Write-Host "===================================" -ForegroundColor $Cyan
    Write-Host " Aplicação iniciada com sucesso!" -ForegroundColor $Green
    Write-Host " API: http://localhost:8080" -ForegroundColor $Cyan
    Write-Host " Adminer: http://localhost:8081" -ForegroundColor $Cyan
    Write-Host " H2 Console: http://localhost:8080/h2-console" -ForegroundColor $Cyan
    Write-Host "===================================" -ForegroundColor $Cyan
    
    # Mostrar os containers em execução
    Write-Host "`nContainers em execução:" -ForegroundColor $Green
    docker ps --format "table {{.Names}}\t{{.Status}}\t{{.Ports}}"
    
    # Se for modo dev, mostrar como parar
    if ($Mode -ne "prod") {
        Write-Host "`nPara parar a aplicação, pressione Ctrl+C" -ForegroundColor $Yellow
    } else {
        Write-Host "`nPara parar a aplicação em produção:" -ForegroundColor $Yellow
        Write-Host "docker-compose down" -ForegroundColor $Cyan
    }
} else {
    Write-Host " Erro ao iniciar a aplicação" -ForegroundColor $Red
    Write-Host "Código de erro: $LASTEXITCODE" -ForegroundColor $Red
    exit $LASTEXITCODE
}