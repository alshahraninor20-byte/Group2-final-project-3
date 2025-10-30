resource "azurerm_resource_group" "rg" {
  name     = local.resource_group_name
  location = local.location
}

module "vnet" {
  source              = "./modules/azurerm_vnet"
  vnet_name           = local.vnet_name
  resource_group_name = azurerm_resource_group.rg.name
  location            = local.location
  address_space       = local.address_space
}

module "subnets" {
  source              = "./modules/azurerm_subnets"
  for_each            = local.subnet
  name                = each.key
  resource_group_name = azurerm_resource_group.rg.name
  vnet_name           = module.vnet.virtual_network
  address_prefixes    = each.value.address_space
}

module "sql" {
  source              = "./modules/azurerm_sql"
  resource_group_name = azurerm_resource_group.rg.name
  location            = local.location
  db_name             = "group2-project3-db"
  admin_login         = var.admin_user
  admin_password      = var.admin_password

}

module "pri" {
  source              = "./modules/azurerm_privatelink"
  location            = local.location
  resource_group_name = azurerm_resource_group.rg.name
  db_name             = module.sql.db_name
  subnet_id           = module.subnets["db_subnet"].id
  vnet_id             = module.vnet.virtual_network_id
  admin_login         = var.admin_user
  mssql_server        = lower(module.sql.mssql_server)
}

module "key_vault" {
  source               = "./modules/azurerm_vault"
  name                 = "group2-kv"
  location             = local.location
  resource_group_name  = azurerm_resource_group.rg.name
  subnet_ids           = [module.subnets["aks_subnet"].id]
}

module "aks" {
  source                 = "./modules/azurerm_aks"
  prefix                 = "group2-project"
  location               = local.location
  resource_group_name    = azurerm_resource_group.rg.name
  default_node_pool_name = "kha"
  vm_size                = "Standard_A2_v2"
  subnet                 = module.subnets["aks_subnet"].id
}

