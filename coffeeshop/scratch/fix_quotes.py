import re

file_path = r'd:\Project\BTL-CNPM\coffeeshop\sql\schema.sql'
with open(file_path, 'r', encoding='utf-8') as f:
    content = f.read()

# Fix the escaped quotes that were accidentally introduced
content = content.replace("\\'", "'")

with open(file_path, 'w', encoding='utf-8') as f:
    f.write(content)
print('Fixed schema.sql quotes')
