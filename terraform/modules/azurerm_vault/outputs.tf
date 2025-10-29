output "key_vault_uri" {
  description = "URI of Key Vault"
  value       = azurerm_key_vault.kvault.vault_uri
}

output "key_vault_name" {
  description = "Key Vault name"
  value       = azurerm_key_vault.kvault.name
}
