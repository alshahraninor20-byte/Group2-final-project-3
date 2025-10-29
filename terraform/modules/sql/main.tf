resource "azurerm_mssql_server" "main" {
  name                         = "${var.db_name}-sqlsrv"
  resource_group_name           = var.resource_group_name
  location                      = var.location
  version                       = "12.0"
  administrator_login           = var.admin_login
  administrator_login_password  = var.admin_password
  public_network_access_enabled = false
  minimum_tls_version           = "1.2"
}

resource "azurerm_mssql_database" "db" {
  name           = "${var.db_name}-db"
  server_id      = azurerm_mssql_server.main.id
  sku_name       = "Basic"
}