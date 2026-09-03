# Tester App
Create and solve tests to study for your exams.

## What does this thing look like?
<img width="2560" height="1440" alt="image" src="https://github.com/user-attachments/assets/c0a6383a-c2d5-4cba-8f2d-8ae172cfab03" />
<img width="2560" height="1440" alt="image" src="https://github.com/user-attachments/assets/cd6f7245-058e-4538-a05d-24cec3fa4f7c" />
<img width="2560" height="1440" alt="image" src="https://github.com/user-attachments/assets/2c1a9cae-42f6-436b-b35e-8d971542d5f9" />
<img width="2560" height="1440" alt="image" src="https://github.com/user-attachments/assets/60019f46-2a59-4cea-a66e-95ea2f385274" />
<img width="2560" height="1440" alt="image" src="https://github.com/user-attachments/assets/94aa72e7-8533-4019-9c0d-23ac0710b910" />
<img width="2560" height="1440" alt="image" src="https://github.com/user-attachments/assets/9316a59d-801d-4f1a-af2b-cae5450bfe20" />
<img width="2560" height="1440" alt="image" src="https://github.com/user-attachments/assets/de7c6749-7260-4036-ba33-48221f6bc3cd" />

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
### Image Formats
- .png
- .jpg

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
        WRITTEN ORDERED {
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
| - | Written Question, can be ORDERED |

If **Type** is left empty it will be set to *WRITTEN*.

A **TextOption** has to contain an **Array** of any amount of **String** *text*.

A **ButtonOption** has to contain a **String** *text* and a **Boolean** of value *true* or *false*.

True/False Questions have to contain a **Boolean** of value *true* or *false*.
