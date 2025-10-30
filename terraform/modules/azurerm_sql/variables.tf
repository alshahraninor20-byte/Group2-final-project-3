variable "resource_group_name" {
    type = string
}
variable "location" {
    type = string
}
variable "db_name" {
    type = string
}

variable "admin_login" {
    type = string
}

variable "admin_password" {
    type = string
  sensitive = true
}

variable "vnet_id" {
  type = string
}

variable "subnet_id" {
  type = string
}
