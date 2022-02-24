package com.test.rbcaccountpoc.utils

class Currency {

    companion object {
        fun formatBalance(balance: String ): String {
            return if (balance.isNotEmpty() && balance.contains("-")) {
                 "-$".plus(balance.removePrefix("-"))
            } else {
                "$$balance"
            }
        }
    }
}