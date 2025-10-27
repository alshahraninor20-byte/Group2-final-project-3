output "key_vault_uri" {
  description = "URI of Key Vault"
  value       = azurerm_key_vault.kvault.vault_uri
}

