locals {
 resource_group_name = "prg3-group2"
  vnet_name           = "k"
  location            = "Chile Central"
  tags = {
    bootcamp = "devops-group2"
  }
  address_space = ["10.0.0.0/16"]

  subnet = {
    db_subnet = {
      address_space = ["10.0.0.0/24"]
    }
    aks_subnet = {
      address_space = ["10.0.2.0/23"]
    }
    
  }


}



