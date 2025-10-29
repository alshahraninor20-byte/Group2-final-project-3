output "db_host" {
  value = "${azurerm_mssql_server.main.name}.database.windows.net"
}

output "db_name" {
  value = azurerm_mssql_database.db.name
}

output "db_username" {
  value = azurerm_mssql_server.main.administrator_login
}

output "mssql_server" {
  value = azurerm_mssql_server.main.id
}