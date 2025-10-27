resource "azurerm_mssql_server" "main" {
  name                         = "${var.db_name}-server-group2"
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
  zone_redundant      = false
}


resource "azurerm_private_endpoint" "sql_private_ep" {
  name                = "${var.db_name}-private-endpoint"
  location            = var.location
  resource_group_name = var.resource_group_name
  subnet_id           = var.subnet_id

  private_service_connection {
    name                           = "sql-private-conn"
    private_connection_resource_id = azurerm_mssql_server.main.id
    subresource_names              = ["sqlServer"]
    is_manual_connection           = false
  }
}

resource "azurerm_private_dns_zone" "sql" {
  name                = "privatelink.database.windows.net"
  resource_group_name = var.resource_group_name
}


resource "azurerm_private_dns_zone_virtual_network_link" "sql_link" {
  name                  = "${var.db_name}-dns-link"
  resource_group_name   = var.resource_group_name
  private_dns_zone_name = azurerm_private_dns_zone.sql.name
  virtual_network_id    = var.vnet_id
}


resource "azurerm_private_dns_a_record" "sql_record" {
  name                = azurerm_mssql_server.main.name
  zone_name           = azurerm_private_dns_zone.sql.name
  resource_group_name = var.resource_group_name
  ttl                 = 300
  records             = [azurerm_private_endpoint.sql_private_ep.private_service_connection[0].private_ip_address]
}