output "sql_private_ip" {
  value = azurerm_private_endpoint.sql_private_ep.private_service_connection[0].private_ip_address
}
