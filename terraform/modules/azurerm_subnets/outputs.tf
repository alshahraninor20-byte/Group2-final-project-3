# change output from a single resource output to a grouped/mapped output
output "subnets" {
  value = {
    db_subnet  = azurerm_subnet.db_subnet.id
    aks_subnet = azurerm_subnet.aks_subnet.id
  }
}
