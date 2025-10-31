resource "azurerm_kubernetes_cluster" "aks" {
  name                = "${var.prefix}-aks-cluster"
  location            = var.location
  resource_group_name = var.resource_group_name 
  dns_prefix          = "${var.prefix}-dns"

  node_resource_group = "${var.prefix}-aks-node"

  default_node_pool {
    name                 = var.default_node_pool_name
    node_count           = 1
    vm_size              = var.vm_size
    vnet_subnet_id       = var.subnet
    type                 = "VirtualMachineScaleSets"
  }

  identity {
    type = "SystemAssigned"
  }

  network_profile {
    network_plugin = "azure"
    service_cidr   = "10.0.3.0/24"
    dns_service_ip = "10.0.3.10"                               
    load_balancer_sku = "standard"
  }

}

resource "azurerm_kubernetes_cluster_node_pool" "user_pool" {
  name                  = "userpool"
  kubernetes_cluster_id = azurerm_kubernetes_cluster.aks.id
  vm_size               = var.vm_size
  temporary_name_for_rotation    = "tempname"
  auto_scaling_enabled = true
  node_count            = 1
  min_count             = 1
  max_count             = 2

  lifecycle {
    ignore_changes = [
      node_count
    ]
  }

}
