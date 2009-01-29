package bom.examples.schemas

import bom.schema._
import bom.types._

object JavaClassSchema extends BOMSchema with BOMSchemaBuilder with BOMTypes {

  def schema = document {
    classDefinition
  }

  def classDefinition =
    sequence("class") {
      number("magic", bom_uint)
      sequence("version") {
        number("minor", bom_ushort)
        number("major", bom_ushort)
      }
      number("constant_pool_count", bom_ushort)
      array("constant_pool", "../constant_pool_count - 1", irregular) {
        reference(constantType)
      }
      number("access_flags", bom_ushort, {
        masks {
          mask("ACC_PUBLIC", "0x0001")
          mask("ACC_FINAL", "0x0010")
          mask("ACC_SUPER", "0x0020")
          mask("ACC_INTERFACE", "0x0200")
          mask("ACC_ABSTRACT", "0x0400")
        }
      })
      number("this_class", bom_ushort)
      number("super_class", bom_ushort)
      number("interfaces_count", bom_ushort)
      array("interfaces", "../interfaces_count", irregular) {
        reference(interfaceType)
      }
      number("fields_count", bom_ushort)
      array("fields", "../fields_count", irregular) {
        reference(fieldType)
      }
      number("methods_count", bom_ushort)
      array("methods", "../methods_count", irregular) {
        reference(methodType)
      }
      number("attributes_count", bom_ushort)
      array("attributes", "../attributes_count", irregular) {
        reference(attributeType)
      }
    }

  def constantType =
    switch("number(bom:previous-sibling()/tag)") {
      when("5") {
        sequence("constant") {}
      }
      when("6") {
        sequence("constant") {}
      }
      when("*") {
        sequence("constant") {
          number("tag", bom_ubyte,  {
            map {
              value("1", "Utf8")
              value("3", "Integer")
              value("4", "Float")
              value("5", "Long")
              value("6", "Double")
              value("7", "Class")
              value("8", "String")
              value("9", "FieldRef")
              value("10", "MethodRef")
              value("11", "InterfaceMethodRef")
              value("12", "NameAndType")
              value("*", "UNKNOWN")
            }
          })
          switch("../tag") {
            when("1") {
              sequence("content") {
                number("length", bom_ushort)
                string("bytes", "utf-8", byteSize("../length"))
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
      number("access_flags", bom_ushort, {
        masks {
          mask("ACC_PUBLIC", "0x0001")
          mask("ACC_PRIVATE", "0x0002")
          mask("ACC_PROTECTED", "0x0004")
          mask("ACC_STATIC", "0x0008")
          mask("ACC_FINAL", "0x0010")
          mask("ACC_VOLATILE", "0x0040")
          mask("ACC_TRANSIENT", "0x0080")
        }
      })
      number("name_index", bom_ushort)
      number("descriptor_index", bom_ushort)
      number("attributes_count", bom_ushort)
      array("attributes", "../attributes_count") {
        reference(fieldAttributeType)
      }
    }

  def methodType =
    sequence("method") {
      number("access_flags", bom_ushort, {
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
      })
      number("name_index", bom_ushort)
      number("descriptor_index", bom_ushort)
      number("attributes_count", bom_ushort)
      array("attributes", "../attributes_count") {
        reference(methodAttributeType)
      }
    }

  def attributeType =
    sequence("attribute") {
      number("attribute_name_index", bom_ushort)
      number("attribute_length", bom_uint)
      switch("/class/constant_pool/constant[number(bom:context()/../attribute_name_index[1])]/content/bytes") {
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
            array("classes", "../number_of_classes", true) {
              sequence("inner_class") {
                number("inner_class_info_index", bom_ushort)
                number("outer_class_info_index", bom_ushort)
                number("inner_name_index", bom_ushort)
                number("inner_class_access_flags", bom_ushort, {
                  masks {
                    mask("ACC_PUBLIC", "0x0001")
                    mask("ACC_PRIVATE", "0x0002")
                    mask("ACC_PROTECTED", "0x0004")
                    mask("ACC_STATIC", "0x0008")
                    mask("ACC_FINAL", "0x0010")
                    mask("ACC_INTERFACE", "0x0200")
                    mask("ACC_ABSTRACT", "0x0400")
                  }
                })
              }
            }
          }
        }
        when("*") {
          array("info", "../attribute_length") {
            number("content", bom_ubyte)
          }
        }
      }
    }

  def fieldAttributeType =
    sequence("field_attribute") {
      number("attribute_name_index", bom_ushort)
      number("attribute_length", bom_uint)
      switch("/class/constant_pool/constant[number(bom:context()/../attribute_name_index[1])]/content/bytes") {
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
          array("info", "../attribute_length") {
            number("content", bom_ubyte)
          }
        }
      }
    }

  def codeAttributeType =
    sequence("code_attribute") {
      number("attribute_name_index", bom_ushort)
      number("attribute_length", bom_uint)
      switch("/class/constant_pool/constant[number(bom:context()/../attribute_name_index[1])]/content/bytes") {
        when("LineNumberTable") {
          sequence("line_numbers") {
            number("line_number_table_length", bom_ushort)
            array("line_number_table", "../line_number_table_length") {
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
            array("local_variable_table", "../local_variable_table_length") {
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
          array("info", "../attribute_length") {
            number("content", bom_ubyte)
          }
        }
      }
    }

  def methodAttributeType =
    sequence("method_attribute") {
      number("attribute_name_index", bom_ushort)
      number("attribute_length", bom_uint)
      switch("/class/constant_pool/constant[number(bom:context()/../attribute_name_index[1])]/content/bytes") {
        when("Code") {
          sequence("bytecode") {
            number("max_stack", bom_ushort)
            number("max_locals", bom_ushort)
            number("code_length", bom_uint)
            reference(bytecodeBlock)
            number("exception_table_length", bom_ushort)
            array("exception_table", "../exception_table_length") {
              sequence("exception") {
                number("start_pc", bom_ushort)
                number("end_pc", bom_ushort)
                number("handler_pc", bom_ushort)
                number("catch_type", bom_ushort)
              }
            }
            number("attributes_count", bom_ushort)
            array("attributes", "../attributes_count") {
              reference(codeAttributeType)
            }
          }
        }
        when ("Exceptions") {
          sequence("exceptions") {
            number("number_of_exceptions", bom_ushort)
            array("exception_index_table", "../number_of_exceptions") {
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
          array("info", "../attribute_length") {
            number("content", bom_ubyte)
          }
        }
      }
    }

  def bytecodeBlock =
    array("code", "../code_length") {
      number("content", bom_ubyte)
    }
  
  def main(args: Array[String]) = {
    println(classDefinition.getClass)
  }
  
}

/*
    <!-- bom:array name="code" size="../code_length">
      <bom:sequence name="operation">
        <bom:number name="opcode" type="unsigned-byte" />
        <bom:switch expr="../opcode">
          <bom:case value="1">
            <bom:sequence name="aconst_null" />
          </bom:case>
          <bom:case value="16">
            <bom:sequence name="bipush">
              <bom:number name="byte" type="unsigned-byte" />
            </bom:sequence>
          </bom:case>
          <bom:case value="25">
            <bom:sequence name="aload">
              <bom:number name="index" type="unsigned-byte" />
            </bom:sequence>
          </bom:case>
          <bom:case value="42">
            <bom:sequence name="aload_0" />
          </bom:case>
          <bom:case value="43">
            <bom:sequence name="aload_1" />
          </bom:case>
          <bom:case value="44">
            <bom:sequence name="aload_2" />
          </bom:case>
          <bom:case value="45">
            <bom:sequence name="aload_3" />
          </bom:case>
          <bom:case value="50">
            <bom:sequence name="aaload" />
          </bom:case>
          <bom:case value="51">
            <bom:sequence name="baload" />
          </bom:case>
          <bom:case value="58">
            <bom:sequence name="astore">
              <bom:number name="index" type="unsigned-byte" />
            </bom:sequence>
          </bom:case>
          <bom:case value="75">
            <bom:sequence name="astore_0" />
          </bom:case>
          <bom:case value="76">
            <bom:sequence name="astore_1" />
          </bom:case>
          <bom:case value="77">
            <bom:sequence name="astore_2" />
          </bom:case>
          <bom:case value="78">
            <bom:sequence name="astore_3" />
          </bom:case>
          <bom:case value="83">
            <bom:sequence name="aastore" />
          </bom:case>
          <bom:case value="84">
            <bom:sequence name="bastore" />
          </bom:case>
          <bom:case value="89">
            <bom:sequence name="dup" />
          </bom:case>
          <bom:case value="176">
            <bom:sequence name="areturn" />
          </bom:case>
          <bom:case value="183">
            <bom:sequence name="invokespecial">
              <bom:number name="indexbyte1" type="unsigned-byte" />
              <bom:number name="indexbyte2" type="unsigned-byte" />
            </bom:sequence>
          </bom:case>
          <bom:case value="187">
            <bom:sequence name="new">
              <bom:number name="indexbyte1" type="unsigned-byte" />
              <bom:number name="indexbyte2" type="unsigned-byte" />
            </bom:sequence>
          </bom:case>
          <bom:case value="189">
            <bom:sequence name="anewarray">
              <bom:number name="indexbyte1" type="unsigned-byte" />
              <bom:number name="indexbyte2" type="unsigned-byte" />
            </bom:sequence>
          </bom:case>
          <bom:case value="190">
            <bom:sequence name="arraylength" />
          </bom:case>
          <bom:case value="191">
            <bom:sequence name="athrow" />
          </bom:case>
          <bom:case value="*">
            <bom:sequence name="UNKNOWN" />
          </bom:case>
        </bom:switch>
      </bom:sequence -->
*/
