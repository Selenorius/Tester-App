![App Icon](app/src/main/resources/tester_appx96.png)
# Tester App
Create and solve tests to study for your exams.

## What does this thing look like?
### The Menu
<img width="960" height="720" alt="image" src="https://github.com/user-attachments/assets/d806e786-b0fe-4dc3-bdcb-b2fd63ae14e3" />

### The Editor
<img width="960" height="720" alt="image" src="https://github.com/user-attachments/assets/a296629b-680d-4361-89b0-2eaf50e1cbc6" />

### The Questions
<img width="960" height="720" alt="image" src="https://github.com/user-attachments/assets/b7d4a82c-633a-4c64-a7a1-a7ad777e6922" />

## Download
[Releases](https://github.com/Selenorius/Tester-App/releases)

## Guide
### Step 1: Create a topic
1. Click on the big *PLUS* button
2. Choose *Create new topic*

### Step 2: Create an exam
1. Click on the *Add exam* button
2. Click on the big *PLUS* button
3. Choose an option

### Step 3: Solve your exam
1. Start **Tester App.jar**.
2. Pick your **Topic**.
3. Pick your **Exam**.

Now you can solve your **Exam**!

## Help
### Jar Location
The **Jar** file is in *tester_app/app/build/libs*, but you can also find it in [releases](https://github.com/Selenorius/Tester-App/releases).

### Create an exam by hand
1. Open/create a directory and create a new textfile with the name of your exam.
2. Now open the textfile created in step 1 and add your questions in the following format:

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
        MULTIPLE_CHOICE ORDERED {
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