variable "name" {
  type        = string
  description = "Key Vault name"
}

variable "location" {
  type        = string
  description = "Azure region"
}

variable "resource_group_name" {
  type        = string
  description = "Resource Group name"
}

variable "sku_name" {
  type        = string
  description = "Key Vault SKU"
  default     = "standard"
}

variable "subnet_ids" {
  type        = list(string)
  description = "Subnets allowed to access the vault"
}


