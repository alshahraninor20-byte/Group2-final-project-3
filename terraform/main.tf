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
  resource_group_name = module.resource_group.resource_group
  vnet_name           = module.virtual_network.virtual_network
  address_prefixes    = each.value.address_space
}



