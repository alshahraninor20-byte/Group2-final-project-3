module "resource_group" {
  source                 = "./modules/azurerm_resource_group"
  name                   = local.resource_group_name
  location               = local.location
}

module "virtual_network" {
  source              = "./modules/azurerm_vnet"
  name                = local.vnet_name
  address_space       = local.address_space
  resource_group_name = module.resource_group.resource_group
  location            = local.location
}


module "subnets" {
  source              = "./modules/azurerm_subnets"
  resource_group_name = module.resource_group.resource_group
  vnet_name           = module.virtual_network.virtual_network
}

module "sql" {
  source              = "./modules/azurerm_sql"
  resource_group_name = module.resource_group.resource_group
  location            = local.location
  db_name             = "project3"
  admin_login         = var.admin_user
  admin_password      = var.admin_password
  subnet_id           = module.subnets.subnets["db_subnet"]
  vnet_id             = module.virtual_network.virtual_network_id
}

module "aks" {
  source                  = "./modules/azurerm_aks"
  prefix                  = "group2"
  location                = local.location
  vm_size                 = "Standard_B2s"
  default_node_pool_name  = "nodepool"
  resource_group_name     = module.resource_group.resource_group
  aks_subnet_id           = module.subnets.subnets["aks_subnet"]
}
