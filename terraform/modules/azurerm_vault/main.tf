data "azurerm_client_config" "current" {}

resource "azurerm_key_vault" "kvault" {
  name                        = var.name
  location                    = var.location
  resource_group_name         = var.resource_group_name
  tenant_id                   = data.azurerm_client_config.current.tenant_id
  sku_name                     = var.sku_name
  enabled_for_deployment       = true
  public_network_access_enabled = true
  rbac_authorization_enabled    = false

  access_policy {
  tenant_id = data.azurerm_client_config.current.tenant_id
  object_id = data.azurerm_client_config.current.object_id

    key_permissions    = ["Get", "List", "Update", "Delete"]
    secret_permissions = ["Get", "List", "Set", "Delete"]
    certificate_permissions = ["Get", "List", "Update", "Delete", "Recover", "Backup", "Restore"]
  }

}