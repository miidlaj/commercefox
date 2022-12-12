package com.ecommerce.review.vote;

public enum VoteType {

    UP {
        @Override
        public String toString() {
            return "up";
        }
    },
    DOWN {
        @Override
        public String toString() {
            return "down";
        }
    }
}
