variable "resource_group_name" {
  description = "Name of the resource group"
}

variable "name" {
  description = "Subnet name"
}

variable "vnet_name" {
  description = "Name of the virtual network"
}

variable "address_prefixes" {
  description = "Address prefixes for the subnet"
  default     = []
}

