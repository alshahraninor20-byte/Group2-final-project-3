variable "resource_group_name" {}
variable "location" {}
variable "db_name" {}
variable "admin_login" {}
variable "admin_password" {
  sensitive = true
}
variable "subnet_id" {}
variable "vnet_id" {
}