variable "name" {
  type = string
} 

variable "resource_group_name" {
  type = string
}

variable "vnet_name" {
  type = string
}


variable "address_prefixes" {
    default     = []
}
