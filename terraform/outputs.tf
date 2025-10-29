output "kube_config" {
  value = module.aks.kube_config
  sensitive = true
}

output "db_server" {
  value = module.sql.db_host
}

output "db_name" {
  value = module.sql.db_name
}

output "rg" {
  value = azurerm_resource_group.rg.name  
}

output "cluster_name" {
  value = module.aks.cluster_name
}