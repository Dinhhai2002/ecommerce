module.exports = function (plop) {
    plop.setGenerator("spring-boot-crud", {
      description: "Generate DAO, Service, and Controller for a Spring Boot entity",
      prompts: [
        {
          type: "input",
          name: "entity",
          message: "Enter the entity name (e.g., Category):",
        },
      ],
      actions: [
        {
          type: "add",
          path: "src/main/java/com/web/ecommerce/entity/{{entity}}.java",
          templateFile: "plop-templates/entity/entity.hbs",
        },
        {
          type: "add",
          path: "src/main/java/com/web/ecommerce/dao/{{entity}}Dao.java",
          templateFile: "plop-templates/dao/dao.hbs",
        },
        {
          type: "add",
          path: "src/main/java/com/web/ecommerce/dao/impl/{{entity}}DaoImpl.java",
          templateFile: "plop-templates/dao/daoImpl.hbs",
        },
        {
          type: "add",
          path: "src/main/java/com/web/ecommerce/service/{{entity}}Service.java",
          templateFile: "plop-templates/service/service.hbs",
        },
        {
          type: "add",
          path: "src/main/java/com/web/ecommerce/service/impl/{{entity}}ServiceImpl.java",
          templateFile: "plop-templates/service/serviceImpl.hbs",
        },
        {
          type: "add",
          path: "src/main/java/com/web/ecommerce/controller/{{entity}}Controller.java",
          templateFile: "plop-templates/controller/controller.hbs",
        },
        {
          type: "add",
          path: "src/main/java/com/web/ecommerce/request/CRUD{{entity}}Request.java",
          templateFile: "plop-templates/request/request.hbs",
        },
        {
          type: "add",
          path: "src/main/java/com/web/ecommerce/response/{{entity}}Response.java",
          templateFile: "plop-templates/response/response.hbs",
        },
      ],
    });
    plop.setHelper("lowerCase", function (text) {
        return text.charAt(0).toLowerCase() + text.slice(1);
      });
    plop.setHelper("upperCase", function (text) {
        return text.toUpperCase();
    });
    plop.setHelper("snakeCase", function (text) {
      return text.replace(/([a-z])([A-Z])/g, '$1_$2').toLowerCase();
  });
  };
  