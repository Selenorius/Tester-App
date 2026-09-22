# Tester App
Create and solve tests to study for your exams.

## What does this thing look like?
<img width="2560" height="1440" alt="image" src="https://github.com/user-attachments/assets/3d438b73-3eb0-4a9c-ae5f-8fe83c295f15" />
<img width="2560" height="1440" alt="image" src="https://github.com/user-attachments/assets/28cb9cf8-3643-4e98-9950-965fad940d6a" />
<img width="2560" height="1440" alt="image" src="https://github.com/user-attachments/assets/87cf0cd0-3296-4254-ac6b-17dfb19f582e" />
<img width="2560" height="1440" alt="image" src="https://github.com/user-attachments/assets/b3111de3-b4f2-4662-a1c1-9db5085ebfb1" />
<img width="2560" height="1440" alt="image" src="https://github.com/user-attachments/assets/2ee9235c-f3e4-4930-967b-524336ac2db3" />

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
1. Start **Tester App**.
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
