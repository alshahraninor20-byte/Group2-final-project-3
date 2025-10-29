output "virtual_network" {
  value = azurerm_virtual_network.vnet.name
}

output "virtual_network_id" {
  value = azurerm_virtual_network.vnet.id
}
