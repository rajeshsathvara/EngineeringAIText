package com.engineeringaitest.model;

import java.util.List;

public class APIResponse {
    private boolean status;
    private String message;
    private Data data;

    public Data getData() {
        return this.data;
    }

    public class Data {

        private List<Users> users;
        private boolean has_more;

        public List<Users> getUsers() {
            return this.users;
        }

        public class Users {
            private String name;
            private String image;
            private List<String> items;

            public String getName() {
                return this.name;
            }

            public String getImage() {
                return this.image;
            }

            public List<String> getItems() {
                return this.items;
            }
        }
    }
}

