terraform {
  required_providers {
    azurerm = {
      source  = "hashicorp/azurerm"
      version = "~> 4.26.0"
    }
  }
}

provider "azurerm" {
  # Configuration options

  features {

  }

  subscription_id = "80646857-9142-494b-90c5-32fea6acbc41"
}