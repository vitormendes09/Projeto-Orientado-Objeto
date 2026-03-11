param(
    [string]$Mode = "dev"
)

Write-Host "Construindo a imagem Docker da aplicação..." -ForegroundColor Green

if ($Mode -eq "prod") {
    Write-Host "Modo produção" -ForegroundColor Yellow
    docker-compose -f docker-compose.yml -f docker-compose.prod.yml up -d --build
} else {
    Write-Host "Modo desenvolvimento" -ForegroundColor Cyan
    docker-compose up --build
}