resource "azurerm_kubernetes_cluster" "aks" {
  name                = "${var.prefix}-aks-cluster"
  location            = var.location
  resource_group_name = var.resource_group_name 
  dns_prefix          = "${var.prefix}-dns"

  node_resource_group = "${var.prefix}-aks-node"

  default_node_pool {
    name       = var.default_node_pool_name
    node_count = 2
    vm_size    = var.vm_size
    vnet_subnet_id = var.subnet
  }

  identity {
    type = "SystemAssigned"
  }

  network_profile {
  network_plugin     = "azure"
  network_plugin_mode = "overlay"
  service_cidr       = "10.240.0.0/16"
  dns_service_ip     = "10.240.0.10"
}


 
}
