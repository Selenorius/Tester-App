![App Icon](app/src/main/resources/tester_appx96.png)
# Tester App
Create and solve tests to study for your exams.

## Download
[Releases](https://github.com/Selenorius/Tester-App/releases)

## Guide
### Step 1: Create a topic
1. Open the Editor from the **Tester** Menu.
2. Create a new directory inside the *topics* directory with your desired topic name.

### Step 2: Create an exam
1. Open the directory created in step 1 and create a new textfile with the name of your exam.
2. Now open the textfile created in step 2 and add your questions in the following format:

```r
{
    {
        TRUE_FALSE {
            "True/False Question text"
            {
                TRUE
            }
        }
    }
    {
        MULTIPLE_CHOICE [ORDERED] {
            "Multiple Choice Question text"
            {
                TRUE {
                    "Option 1 text"
                    "Option 1 alternative text"
                }
                FALSE {
                    "Option 2 text"
                }
                FALSE {
                    "Option 3 text"
                }
            }
        }
    }
    {
        WRITTEN {
            "Written Question text"
            {
                TRUE {
                    "Option 1 text"
                    "Option 1 alternative text"
                }
                FALSE {
                    "Option 2 text"
                }
                FALSE {
                    "Option 3 text"
                }
            }
        }
    }
}
```

| Enum | Description |
|:--------------- |:-------------------------- |
| MULTIPLE_CHOICE or MC | Multiple Choice Question, can be ORDERED |
| TRUE_FALSE or TF | True/False Question |
| - | Written Question |

If **Type** is left empty it will be set to *WRITTEN*.

A **TextOption** has to contain an **Array** of any amount of **String** *text*.

A **ButtonOption** has to contain a **String** *text* and a **Boolean** of value *true* or *false*.

True/False Questions have to contain a **Boolean** of value *true* or *false*.

### Step 3: Solve your exam
1. Start **tester_app.jar**.
2. Pick your **Topic**.
3. Pick your **Exam**.

Now you can solve your **Exam**!

## Help
### Jar Location
The **Jar** file is in *tester_app/app/build/libs*, but you can also find it in [releases](https://github.com/Selenorius/Tester-App/releases).