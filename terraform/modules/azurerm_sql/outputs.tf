output "db_host" {
  value = "${azurerm_mssql_server.main.name}.database.windows.net"
}

output "db_port" {
  value = 1433
}

output "db_name" {
  value = azurerm_mssql_database.db.name
}

output "db_username" {
  value = azurerm_mssql_server.main.administrator_login
}

output "db_password" {
  value     = var.admin_password
  sensitive = true
}

output "sql_private_ip" {
  value = azurerm_private_endpoint.sql_private_ep.private_service_connection[0].private_ip_address
}
output "sql_server_id" {
  description = "The Resource ID of the SQL Server"
  value       = azurerm_mssql_server.main.id
}
output "database_id" {
  description = "Resource ID of the SQL database"
  value       = azurerm_mssql_database.db.id
}

output "sql_fqdn_private" {
  value = "${azurerm_mssql_server.main.name}.privatelink.database.windows.net"
  description = "SQL private FQDN"
}
