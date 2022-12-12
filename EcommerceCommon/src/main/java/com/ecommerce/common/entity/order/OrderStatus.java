package com.ecommerce.common.entity.order;

public enum OrderStatus {

    NEW{
        @Override
        public String defaultDescription() {
            return "Order was place by Customer";
        }
    }, CANCELLED {
        @Override
        public String defaultDescription() {
            return "Order was rejected";
        }
    }, PROCESSING {
        @Override
        public String defaultDescription() {
            return "Order is being processed";
        }
    }, PACKAGED {
        @Override
        public String defaultDescription() {
            return "Products were packaged";
        }
    }, PICKED {
        @Override
        public String defaultDescription() {
            return "Shipper picked the package";
        }
    }, SHIPPING {
        @Override
        public String defaultDescription() {
            return "Shipper is delivering the package";
        }
    },
    DELIVERED {
        @Override
        public String defaultDescription() {
            return "Customer received products";
        }
    }, RETURNED {
        @Override
        public String defaultDescription() {
            return "Products were returned";
        }
    }, PAID {
        @Override
        public String defaultDescription() {
            return "Customer has paid this order";
        }
    }, REFUNDED {
        @Override
        public String defaultDescription() {
            return "Customer has been returned";
        }
    }, RETURN_REQUESTED {
        @Override
        public String defaultDescription() {
            return "Customer has requested Order return";
        }
    };

    public abstract String defaultDescription();

    }
