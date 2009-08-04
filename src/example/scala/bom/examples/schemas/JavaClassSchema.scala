package bom.examples.schemas

import bom.schema._
import bom.types._
import bom.BOM._

/**
 * http://java.sun.com/docs/books/jvms/second_edition/html/ClassFile.doc.html
 */
object JavaClassSchema extends Schema with SchemaBuilder {

  def schema = document("class") {
      number("magic", bom_uint)
      sequence("version") {
        number("minor", bom_ushort)
        number("major", bom_ushort)
      }
      number("constant_pool_count", bom_ushort)
      array("constant_pool", length(n => longValue(n / -1 / "constant_pool_count") - 1), irregular) {
        constantType
      }
      number("access_flags", bom_ushort) {
        masks {
          mask("ACC_PUBLIC", "0x0001")
          mask("ACC_FINAL", "0x0010")
          mask("ACC_SUPER", "0x0020")
          mask("ACC_INTERFACE", "0x0200")
          mask("ACC_ABSTRACT", "0x0400")
        }
      }
      number("this_class", bom_ushort)
      number("super_class", bom_ushort)
      number("interfaces_count", bom_ushort)
      array("interfaces", length((n: BOMNode) => longValue(n / -1 / "interfaces_count")), irregular) {
        interfaceType
      }
      number("fields_count", bom_ushort)
      array("fields", length((n: BOMNode) => longValue(n / -1 / "fields_count")), irregular) {
        fieldType
      }
      number("methods_count", bom_ushort)
      array("methods", length((n: BOMNode) => longValue(n / -1 / "methods_count")), irregular) {
        methodType
      }
      number("attributes_count", bom_ushort)
      array("attributes", length((n: BOMNode) => longValue(n / -1 / "attributes_count")), irregular) {
        attributeType
      }
    }

  def constantType =
    // The java class format defines that Long and Double constants will occupy
    // two units in the index, thus the strange looking structure below...
    switch(n => if (n.index > 0) stringValue(n / -1 / (n.index - 1) / "tag") else "*") {
      when("5") {
        sequence("constant") {}
      }
      when("6") {
        sequence("constant") {}
      }
      when("*") {
        sequence("constant") {
          number("tag", bom_ubyte) {
            map {
              value(1, "Utf8")
              value(3, "Integer")
              value(4, "Float")
              value(5, "Long")
              value(6, "Double")
              value(7, "Class")
              value(8, "String")
              value(9, "FieldRef")
              value(10, "MethodRef")
              value(11, "InterfaceMethodRef")
              value(12, "NameAndType")
              default("UNKNOWN")
            }
          }
          switch(n => stringValue(n / -1 / "tag")) {
            when("1") {
              sequence("content") {
                number("length", bom_ushort)
                string("bytes", "utf-8", byteSize((n: BOMNode) => longValue(n / -1 / "length")))
              }
            }
            when("3") {
              sequence("content") {
                number("value", bom_uint)
              }
            }
            when("4") {
              sequence("content") {
                number("value", bom_float)
              }
            }
            when("5") {
              sequence("content") {
                number("high", bom_uint)
                number("low", bom_uint)
              }
            }
            when("6") {
              sequence("content") {
                number("high", bom_uint)
                number("low", bom_uint)
              }
            }
            when("7") {
              sequence("content") {
                number("name_index", bom_ushort)
              }
            }
            when("8") {
              sequence("content") {
                number("string_index", bom_ushort)
              }
            }
            when("9") {
              sequence("content") {
                number("class_index", bom_ushort)
                number("name_and_type_index", bom_ushort)
              }
            }
            when("10") {
              sequence("content") {
                number("class_index", bom_ushort)
                number("name_and_type_index", bom_ushort)
              }
            }
            when("11") {
              sequence("content") {
                number("class_index", bom_ushort)
                number("name_and_type_index", bom_ushort)
              }
            }
            when("12") {
              sequence("content") {
                number("name_index", bom_ushort)
                number("descriptor_index", bom_ushort)
              }
            }
          }
        }
      }
    }

  def interfaceType =
    sequence("interface") {
      number("cp_index", bom_ushort)
    }

  def fieldType =
    sequence("field") {
      number("access_flags", bom_ushort) {
        masks {
          mask("ACC_PUBLIC", "0x0001")
          mask("ACC_PRIVATE", "0x0002")
          mask("ACC_PROTECTED", "0x0004")
          mask("ACC_STATIC", "0x0008")
          mask("ACC_FINAL", "0x0010")
          mask("ACC_VOLATILE", "0x0040")
          mask("ACC_TRANSIENT", "0x0080")
        }
      }
      number("name_index", bom_ushort)
      number("descriptor_index", bom_ushort)
      number("attributes_count", bom_ushort)
      array("attributes", length((n: BOMNode) => longValue(n / -1 / "attributes_count"))) {
        fieldAttributeType
      }
    }

  def methodType =
    sequence("method") {
      number("access_flags", bom_ushort) {
        masks {
          mask("ACC_PUBLIC", "0x0001")
          mask("ACC_PRIVATE", "0x0002")
          mask("ACC_PROTECTED", "0x0004")
          mask("ACC_STATIC", "0x0008")
          mask("ACC_FINAL", "0x0010")
          mask("ACC_SYNCHRONIZED", "0x0020")
          mask("ACC_NATIVE", "0x0100")
          mask("ACC_ABSTRACT", "0x0400")
          mask("ACC_STRICT", "0x0800")
        }
      }
      number("name_index", bom_ushort)
      number("descriptor_index", bom_ushort)
      number("attributes_count", bom_ushort)
      array("attributes", length((n: BOMNode) => longValue(n / -1 / "attributes_count"))) {
        methodAttributeType
      }
    }

  def attributeType =
    sequence("attribute") {
      number("attribute_name_index", bom_ushort)
      number("attribute_length", bom_uint)
      switch(n => stringValue(n.document / "constant_pool" / (intValue(n / -1 / "attribute_name_index") - 1).asInstanceOf[Int] / 1 / "bytes")) {
        when("SourceFile") {
          number("sourcefile_index", bom_ushort)
        }
        when("Synthetic") {
          sequence("dummy") {}
        }
        when("Deprecated") {
          sequence("dummy") {}
        }
        when("InnerClasses") {
          sequence("inner_classes") {
            number("number_of_classes", bom_ushort)
            array("classes", length((n: BOMNode) => longValue(n / -1 / "number_of_classes")), regular) {
              sequence("inner_class") {
                number("inner_class_info_index", bom_ushort)
                number("outer_class_info_index", bom_ushort)
                number("inner_name_index", bom_ushort)
                number("inner_class_access_flags", bom_ushort) {
                  masks {
                    mask("ACC_PUBLIC", "0x0001")
                    mask("ACC_PRIVATE", "0x0002")
                    mask("ACC_PROTECTED", "0x0004")
                    mask("ACC_STATIC", "0x0008")
                    mask("ACC_FINAL", "0x0010")
                    mask("ACC_INTERFACE", "0x0200")
                    mask("ACC_ABSTRACT", "0x0400")
                  }
                }
              }
            }
          }
        }
        when("*") {
          array("info", length((n: BOMNode) => longValue(n / -1 / "attribute_length"))) {
            number("content", bom_ubyte)
          }
        }
      }
    }

  def fieldAttributeType =
    sequence("field_attribute") {
      number("attribute_name_index", bom_ushort)
      number("attribute_length", bom_uint)
      switch(n => n.document / "constant_pool" / (intValue(n / -1 / "attribute_name_index") - 1).asInstanceOf[Int] / 1 / "bytes") {
        when("ConstantValue") {
          number("constantvalue_index", bom_ushort)
        }
        when("Synthetic") {
          sequence("dummy") {}
        }
        when("Deprecated") {
          sequence("dummy") {}
        }
        when("*") {
          array("info", length((n: BOMNode) => longValue(n / -1 / "attribute_length"))) {
            number("content", bom_ubyte)
          }
        }
      }
    }

  def codeAttributeType =
    sequence("code_attribute") {
      number("attribute_name_index", bom_ushort)
      number("attribute_length", bom_uint)
      switch(n => n.document / "constant_pool" / (intValue(n / -1 / "attribute_name_index") - 1).asInstanceOf[Int] / 1 / "bytes") {
        when("LineNumberTable") {
          sequence("line_numbers") {
            number("line_number_table_length", bom_ushort)
            array("line_number_table", length((n: BOMNode) => longValue(n / -1 / "line_number_table_length"))) {
              sequence("entry") {
                number("start_pc", bom_ushort)
                number("line_number", bom_ushort)
              }
            }
          }
        }
        when("LocalVariableTable") {
          sequence("local_variables") {
            number("local_variable_table_length", bom_ushort)
            array("local_variable_table", length((n: BOMNode) => longValue(n / -1 / "local_variable_table_length"))) {
              sequence("local_variable") {
                number("start_pc", bom_ushort)
                number("length", bom_ushort)
                number("name_index", bom_ushort)
                number("descriptor_index", bom_ushort)
                number("index", bom_ushort)
              }
            }
          }
        }
        when("*") {
          array("info", length((n: BOMNode) => longValue(n / -1 / "attribute_length"))) {
            number("content", bom_ubyte)
          }
        }
      }
    }

  def methodAttributeType =
    sequence("method_attribute") {
      number("attribute_name_index", bom_ushort)
      number("attribute_length", bom_uint)
      switch(n => n.document / "constant_pool" / (intValue(n / -1 / "attribute_name_index") - 1).asInstanceOf[Int] / 1 / "bytes") {
        when("Code") {
          sequence("bytecode") {
            number("max_stack", bom_ushort)
            number("max_locals", bom_ushort)
            number("code_length", bom_uint)
            bytecodeBlock
            number("exception_table_length", bom_ushort)
            array("exception_table", length((n: BOMNode) => longValue(n / -1 / "exception_table_length"))) {
              sequence("exception") {
                number("start_pc", bom_ushort)
                number("end_pc", bom_ushort)
                number("handler_pc", bom_ushort)
                number("catch_type", bom_ushort)
              }
            }
            number("attributes_count", bom_ushort)
            array("attributes", length((n: BOMNode) => longValue(n / -1 / "attributes_count"))) {
              codeAttributeType
            }
          }
        }
        when ("Exceptions") {
          sequence("exceptions") {
            number("number_of_exceptions", bom_ushort)
            array("exception_index_table", length((n: BOMNode) => longValue(n / -1 / "number_of_exceptions"))) {
              number("exception_index", bom_ushort)
            }
          }
        }
        when("Synthetic") {
          sequence("dummy") {}
        }
        when("Deprecated") {
          sequence("dummy") {}
        }
        when("*") {
          array("info", length((n: BOMNode) => longValue(n / -1 / "attribute_length"))) {
            number("content", bom_ubyte)
          }
        }
      }
    }

  def bytecodeBlock =
    array("code", length((n: BOMNode) => longValue(n / -1 / "code_length"))) {
      number("content", bom_ubyte)
    }
  
}
