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
  for_each            = local.subnet
  name                = each.key
  resource_group_name = local.resource_group_name
  vnet_name           = module.virtual_network.virtual_network
  address_prefixes    = each.value.address_space
}

module "aks" {
  source              = "./modules/azurerm_aks" 
  prefix              = "group2"
  location            = local.location
  vm_size             = "Standard_B2s"
  default_node_pool_name = "nodepool"
  resource_group_name = module.resource_group.resource_group
  aks_subnet_id       = module.subnets["aks_subnet"].id
}

module "key_vault" {
  source               = "./modules/azurerm_vault"
  name                 = "group2-kv-t"
  location             = local.location
  resource_group_name  = module.resource_group.resource_group
  subnet_ids           = [module.subnets["aks_subnet"].id]
}
