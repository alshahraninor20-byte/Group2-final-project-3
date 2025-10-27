output "kube_config" {
  value = azurerm_kubernetes_cluster.aks.kube_config_raw

  sensitive = true
}

output "cluster_name" {
  value = azurerm_kubernetes_cluster.aks.name

}

output "aks_managed_identity" {
  value = azurerm_kubernetes_cluster.aks.identity[0].principal_id
}