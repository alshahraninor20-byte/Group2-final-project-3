output "kube_config" {
  value = module.aks.kube_config
  sensitive = true
}

output "cluster_name" {
  value = module.aks.cluster_name
}

output "aks_managed_identity" {
  description = "AKS managed identity"
  value       = module.aks.aks_managed_identity
}

output "db_host" {
  value = module.sql.db_host
}

output "db_name" {
  value = module.sql.db_name
}

output "sql_fqdn_private" {
  value = module.sql.sql_fqdn_private
}