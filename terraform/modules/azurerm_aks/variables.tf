variable "prefix" {
  
}

variable "vm_size" {

    default = "Standard_B2s"
  
}

variable "default_node_pool_name" {
  
}

variable "resource_group_name" {
  description = "Resource group for the private DNS zone"
  type        = string
}

variable "location" {
  description = "Azure region"
  type        = string
}

variable "aks_subnet_id" {
  description = "AKS subnet ID"
  type        = string
}




