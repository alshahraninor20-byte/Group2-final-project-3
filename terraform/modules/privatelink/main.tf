resource "azurerm_private_endpoint" "sql_private_ep" {
  name                = "${var.db_name}-private-endpoint"
  location            = var.location
  resource_group_name = var.resource_group_name
  subnet_id           = var.subnet_id

  private_service_connection {
    name                           = "sql-private-conn"
    private_connection_resource_id = var.mssql_server
    subresource_names              = ["sqlServer"]
    is_manual_connection           = false
  }

  private_dns_zone_group {
  name                 = "default"
  private_dns_zone_ids = [azurerm_private_dns_zone.sql.id]
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
