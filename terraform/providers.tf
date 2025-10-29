terraform {
  required_providers {
    azurerm = {
      source  = "hashicorp/azurerm"
      version = "~> 4.46.0"
    }
  }

    backend "azurerm" {
   resource_group_name = "khalid-rg-tfstate"
   storage_account_name = "khalidstorage0sf7rp"
   container_name = "tfstate"
   key = "terraform.tfstate"
  }
}

provider "azurerm" {
  features {}
}