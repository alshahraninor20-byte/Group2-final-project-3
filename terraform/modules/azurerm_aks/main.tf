resource "azurerm_kubernetes_cluster" "aks" {
  name                = "${var.prefix}cluster-group2"
  location            = var.location
  resource_group_name = var.resource_group_name
  dns_prefix          = "${var.prefix}-dns"

  node_resource_group = "${var.prefix}-aks-node"

  default_node_pool {
    name                 = var.default_node_pool_name
    node_count           = 1
    vm_size              = var.vm_size
    vnet_subnet_id       = var.aks_subnet_id # run it inside aks_subent
    type                 = "VirtualMachineScaleSets"
  }

  identity {
    type = "SystemAssigned"
  }

  network_profile {
    network_plugin = "azure" # Azure CNI (overlay)
    service_cidr   = "10.0.3.0/24"
    dns_service_ip = "10.0.3.10" # From service_cidr IP range
    # For the node auto scaling
    load_balancer_sku = "standard"
  }

  # role_based_access_control_enabled = true

}

resource "azurerm_kubernetes_cluster_node_pool" "user_pool" {
  name                  = "userpool"
  kubernetes_cluster_id = azurerm_kubernetes_cluster.aks.id
  vnet_subnet_id = var.aks_subnet_id # assigning the node pool to a specific subnet
  vm_size               = var.vm_size
  auto_scaling_enabled = true
  node_count            = 1
  min_count             = 1
  max_count             = 2
  # Ignore node_count value when auto scaling is on
  lifecycle {
    ignore_changes = [
      node_count
    ]
  }
}


