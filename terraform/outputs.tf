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

output "kv_uri" {
  description = "Key Vault URI"
  value       = module.key_vault.key_vault_uri
}