locals {
 resource_group_name = "project3-group2"
  location = "UAE North"
  vnet_name = "group2-vent"
  address_space       = ["10.0.0.0/16"]

  subnet = {
    db_subnet = {
      address_space = ["10.0.1.0/24"]
    }
    aks_subnet = {
      address_space = ["10.0.2.0/24"]
    }
    
  }


}
