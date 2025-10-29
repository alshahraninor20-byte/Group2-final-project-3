variable "resource_group_name" {
  description = "Name of the resourc group"
}

variable "location" {
  description = "Location for the virtual network"
}

variable "vnet_name" {
  description = "Name of the virtual network"
}

variable "address_space" {
  description = "Address space for the virtual network"
  default = []
}

variable "tags" {
  description = "Tags for the virtual network"
  default = null
}
