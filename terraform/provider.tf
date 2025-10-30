terraform {
  required_providers {
    azurerm = {
      source  = "hashicorp/azurerm"
      version = "~> 4.46.0"
    }
  }

  backend "azurerm" {
   resource_group_name = "prg3group2-rg-tfstate"
   storage_account_name = "prg3group2storagebghxu0"
   container_name = "tfstate"
   key = "terraform.tfstate"
}
}

provider "azurerm" {
  features {}
subscription_id = "80646857-9142-494b-90c5-32fea6acbc41"
}


